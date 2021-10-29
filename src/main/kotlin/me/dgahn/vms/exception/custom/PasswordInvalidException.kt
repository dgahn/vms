package me.dgahn.vms.exception.custom

import me.dgahn.vms.exception.ErrorCode

class PasswordInvalidException : BadRequestException(ErrorCode.ERROR_102)
