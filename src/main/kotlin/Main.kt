import api.GithubApiService
import api.RetrofitClient
import service.UserService
import ui.TerminalUI

fun main() {
    val api = RetrofitClient.instance.create(GithubApiService::class.java)
    val userService = UserService(api)
    val ui = TerminalUI(userService)
    ui.start()
}