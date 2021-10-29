package me.dgahn.vms.exception.custom

import me.dgahn.vms.exception.ErrorCode

open class CustomException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)
