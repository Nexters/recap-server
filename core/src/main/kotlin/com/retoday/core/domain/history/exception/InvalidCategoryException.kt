package com.retoday.core.domain.history.exception

import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException

class InvalidCategoryException :
    ServerException(
        errorType = ErrorType.WEBSITE_CATEGORY_CLASSIFICATION_FAILED
    )
