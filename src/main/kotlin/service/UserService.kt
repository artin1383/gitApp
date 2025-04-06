package service

import User
import api.GithubApiService
import repository.UserRepository
import retrofit2.HttpException
import java.io.IOException

class UserService(private val api: GithubApiService) {
    suspend fun fetchUser(username: String): User? {
        return try {
            UserRepository.getUser(username)?.let {
                return it
            }

            val userResponse = api.getUser(username)
            if (!userResponse.isSuccessful) {
                throw HttpException(userResponse)
            }

            val userBody = userResponse.body()!!
            val createdAt = userBody.createdAt

            val reposResponse = api.getUserRepos(username)
            if (!reposResponse.isSuccessful) {
                throw HttpException(reposResponse)
            }

            val user = userBody.copy(
                createdAt = createdAt,
                publicRepos = reposResponse.body()!!
            )
            UserRepository.addUser(user)
            user

        } catch (e: HttpException) {
            handleHttpError(e.code())
            null
        } catch (e: IOException) {
            println("❌ خطای شبکه: اتصال به اینترنت برقرار نیست")
            null
        } catch (e: Exception) {
            println("❌ خطای ناشناخته: ${e.message}")
            null
        }
    }

    private fun handleHttpError(code: Int) {
        when (code) {
            404 -> println("⛔ کاربر یافت نشد (کد 404)")
            403 -> println("⛔ دسترسی محدود شده است (کد 403)")
            else -> println("⛔ خطای سرور (کد $code)")
        }
    }
}