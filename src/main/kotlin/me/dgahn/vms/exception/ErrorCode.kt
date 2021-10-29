package me.dgahn.vms.exception

enum class ErrorCode(
    val code: String,
    val message: String
) {
    ERROR_101("ERROR_101", "No User found for ID."),
    ERROR_102("ERROR_102", "The password is wrong."),
    ERROR_103("ERROR_103", "No Vacation found for ID."),
    ERROR_104("ERROR_104", "Vacation already used."),
    ERROR_105("ERROR_105", "No vacations used or insufficient vacation time."),
    ERROR_106("ERROR_106", "You will need to make a more detailed vacation request.."),
}
