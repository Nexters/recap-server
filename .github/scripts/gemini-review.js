const fs = require("fs");
const { GoogleGenerativeAI } = require("@google/generative-ai");

async function main() {
  if (!process.env.GEMINI_API_KEY) {
    console.error("GEMINI_API_KEY is required");
    process.exit(1);
  }

  let diffOutput;
  try {
    diffOutput = fs.readFileSync("diff.txt", "utf8");
  } catch (error) {
    return;
  }

  const maxLength = 30000;
  let isTruncated = false;

  if (diffOutput.length > maxLength) {
    diffOutput = `${diffOutput.substring(0, maxLength)}\n...(Diff truncated due to excessive length)`;
    isTruncated = true;
  }

  const promptTemplate = fs.readFileSync(
    ".github/scripts/review-prompt.md",
    "utf8"
  );
  const prompt = `${promptTemplate}\n${diffOutput}`;

  const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY);
  const model = genAI.getGenerativeModel({ model: "gemini-flash-latest" });

  async function generateWithRetry(retryCount = 0) {
    try {
      const result = await model.generateContent(prompt);
      const response = await result.response;
      return response.text();
    } catch (error) {
      if (error.message.includes("429") || error.message.includes("Too Many Requests")) {
        if (retryCount < 2) {
          console.log("Rate limit hit. Waiting 15 seconds before retry");
          await new Promise((resolve) => setTimeout(resolve, 15000));
          return generateWithRetry(retryCount + 1);
        }
      }
      throw error;
    }
  }

  try {
    const text = await generateWithRetry();

    let footer = "";
    if (isTruncated) {
      footer = `
---
> ⚠️ **주의:** 변경 사항이 너무 많아(${maxLength}자 초과) 일부 코드(뒷부분)는 리뷰에 포함되지 않았습니다.
`;
    }

    const finalComment = `
## 🤖 Gemini Code Review

${text}
`;
    fs.writeFileSync("review_result.txt", finalComment, "utf8");
  } catch (error) {
    console.error("Final Error:", error);
    fs.writeFileSync("review_result.txt", `❌ Review Failed: ${error.message}`);
  }
}

main();
