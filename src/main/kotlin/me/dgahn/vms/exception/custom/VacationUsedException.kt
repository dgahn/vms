package me.dgahn.vms.exception.custom

import me.dgahn.vms.exception.ErrorCode

class VacationUsedException : BadRequestException(ErrorCode.ERROR_104)
