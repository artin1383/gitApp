package repository

import User

object UserRepository {
    private val cachedUsers = mutableMapOf<String, User>()

    fun addUser(user: User) {
        cachedUsers[user.login] = user
    }

    fun getUser(username: String): User? = cachedUsers[username]

    fun searchByRepoName(repoName: String): List<User> {
        return cachedUsers.values.filter { user ->
            user.publicRepos.any { it.name == repoName }
        }
    }

    fun getAllUsers(): List<User> = cachedUsers.values.toList()
}