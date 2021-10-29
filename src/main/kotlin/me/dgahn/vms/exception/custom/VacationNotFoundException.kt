package me.dgahn.vms.exception.custom

import me.dgahn.vms.exception.ErrorCode

class VacationNotFoundException : BadRequestException(ErrorCode.ERROR_103)
