package ui

import kotlinx.coroutines.runBlocking
import repository.UserRepository
import service.UserService
import kotlin.system.exitProcess

class TerminalUI(private val userService: UserService) {
    fun start() {
        while (true) {
            printMenu()
            when (readln()) {
                "1" -> fetchUser()
                "2" -> showAllUsers()
                "3" -> searchByUsername()
                "4" -> searchByRepoName()
                "5" -> exitProcess(0)
                else -> println("❌ انتخاب نامعتبر!")
            }
        }
    }

    private fun printMenu() {
        println(
            """
            1. دریافت اطلاعات کاربر
            2. نمایش لیست کاربران
            3. جستجو بر اساس نام کاربری
            4. جستجو بر اساس نام ریپازیتوری
            5. خروج
        """.trimIndent()
        )
    }

    private fun fetchUser() = runBlocking {
        print("نام کاربری را وارد کنید: ")
        val username = readln()
        val user = userService.fetchUser(username)

        if (user != null) {
            println(
                """✅ کاربر ${user.login} ذخیره شد"""
            )
        } else {
            println("❌ خطا در دریافت اطلاعات کاربر!")
        }
    }

    private fun showAllUsers() {
        val users = UserRepository.getAllUsers()
        if (users.isEmpty()) {
            println("⚠️ هیچ کاربری ذخیره نشده است.")
        } else {
            users.forEach { println(it) }
        }
    }

    private fun searchByUsername() {
        print("نام کاربری را برای جستجو وارد کنید: ")
        val username = readln()
        val user = UserRepository.getUser(username)
        if (user != null) {
            println("✅ کاربر یافت شد:")
            println(user)
        } else {
            println("⚠️ کاربری با نام '$username' وجود ندارد.")
        }
    }

    private fun searchByRepoName() {
        print("نام ریپازیتوری را برای جستجو وارد کنید: ")
        val repoName = readln()
        val users = UserRepository.searchByRepoName(repoName)
        if (users.isNotEmpty()) {
            println("✅ کاربران با ریپازیتوری '$repoName':")
            users.forEach { user ->
                println("نام کاربری: ${user.login}")
            }
        } else {
            println("⚠️ هیچ کاربری با این ریپازیتوری یافت نشد.")
        }
    }
}