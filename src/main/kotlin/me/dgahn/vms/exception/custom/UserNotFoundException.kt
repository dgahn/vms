package me.dgahn.vms.exception.custom

import me.dgahn.vms.exception.ErrorCode

class UserNotFoundException : CustomException(ErrorCode.ERROR_101)
