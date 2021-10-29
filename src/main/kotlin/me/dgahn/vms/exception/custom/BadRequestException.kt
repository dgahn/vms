package me.dgahn.vms.exception.custom

import me.dgahn.vms.exception.ErrorCode

open class BadRequestException(errorCode: ErrorCode) : CustomException(errorCode)
