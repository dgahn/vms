package me.dgahn.vms.exception.custom

import me.dgahn.vms.exception.ErrorCode

class UserNotFoundException : BadRequestException(ErrorCode.ERROR_101)
