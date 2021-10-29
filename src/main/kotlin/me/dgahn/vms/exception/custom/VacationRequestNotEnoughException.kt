package me.dgahn.vms.exception.custom

import me.dgahn.vms.exception.ErrorCode

class VacationRequestNotEnoughException : BadRequestException(ErrorCode.ERROR_104)
