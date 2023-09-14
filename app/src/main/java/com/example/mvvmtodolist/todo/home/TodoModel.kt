package com.example.mvvmtodolist.todo.home

import android.os.Parcelable
import com.example.mvvmtodolist.bookmark.BookmarkModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoModel(
    val id: Long? = null,
    val title: String?,
    val description: String?,
    var isBookmark: Boolean = false
) : Parcelable

// TodoModel 확장함수로 TodoModel -> BookMarkModel 객체로 변환해줌.
fun TodoModel.toBookmarkModel() : BookmarkModel{
    return BookmarkModel(
        id = id ?: 0, //엘비스 연산자 사용 : id가 null일 경우 0 할당
        title = title,
        description =description,
        isBookmark = isBookmark
    )
}
