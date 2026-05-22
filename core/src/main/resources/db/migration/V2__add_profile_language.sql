ALTER TABLE profile
    ADD COLUMN language ENUM ('KO', 'EN', 'JA') NOT NULL DEFAULT 'KO';
