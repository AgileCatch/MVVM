package com.example.mvvmtodolist.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmtodolist.R
import com.example.mvvmtodolist.bookmark.BookmarkModel
import com.example.mvvmtodolist.todo.home.TodoModel

class MainViewModel : ViewModel() {
    val bookmarkState : MutableLiveData<BookmarkState> = MutableLiveData()
    val TodoState : MutableLiveData<TodoState> = MutableLiveData()
}

sealed interface BookmarkState{
    data class AddBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
    data class RemoveBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
    data class ModifyBookmark(val bookmarkModel: BookmarkModel) : BookmarkState
}

sealed interface TodoState{
    data class ModifyTodo(val todoModel: TodoModel) : TodoState
}