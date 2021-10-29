package me.dgahn.vms.exception.custom

import me.dgahn.vms.exception.ErrorCode

class UserVacationsUpdateException : BadRequestException(ErrorCode.ERROR_105)
