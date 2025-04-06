import com.google.gson.annotations.SerializedName

data class User(
    val login: String,
    val followers: Int,
    val following: Int,
    @SerializedName("created_at") val createdAt: String,
    val publicRepos: List<Repository>
)