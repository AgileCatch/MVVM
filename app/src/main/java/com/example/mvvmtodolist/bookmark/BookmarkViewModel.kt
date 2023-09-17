package com.example.mvvmtodolist.bookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmtodolist.todo.home.TodoModel

class BookmarkViewModel : ViewModel() {

    private val _list: MutableLiveData<List<BookmarkModel>> = MutableLiveData()
    val list: LiveData<List<BookmarkModel>> get() = _list
    // getter메소드를 사용하지 않으면
    // 외부에서 list에 대한 변경 사항을 감지하지 못하고 _list를 통해 데이터를 변경해야 한다.
    // 이것은 LiveData의 핵심 장점 중 하나인 데이터 변경 감지 및 생명주기 관리 기능을 사용하지 않는 것과 같다.

    fun findIndex(bookmarkModel: BookmarkModel): Int? {
        val currentList = list.value?.toMutableList()
        val findTodoById = currentList?.find { // 수정하고자 하는 todoModel의 id와 currentList의 id를 비교해 같은 id 를 찾음
            it.id == bookmarkModel.id
        }
        return currentList?.indexOf(findTodoById)//indexOf : 찾고자 하는 Array의 index를 반환
    }

    fun addBookmarkItem(bookmarkModel: BookmarkModel?) {
        if (bookmarkModel == null) {
            return
        }
        val currentList = list.value.orEmpty().toMutableList() // orEmpty -> list.value가 null일 때 빈 리스트 반환
        currentList.add(bookmarkModel)
        _list.value = currentList
    }

    fun removeBookmarkItem(
        bookmarkModel: BookmarkModel,
        position: Int?
    ) {
        fun findIndex(bookmarkModel: BookmarkModel): Int? {
            val currentList = list.value?.toMutableList()
            val findTodoById = currentList?.find { // 수정하고자 하는 todoModel의 id와 currentList의 id를 비교
                it.id == bookmarkModel.id
            }
            return currentList?.indexOf(findTodoById)//indexOf : 찾고자 하는 Array의 index를 반환
        }

        if (bookmarkModel == null) {
            return
        }

        val findPosition = position ?: findIndex(bookmarkModel) //position이 안들어왔을 경우 id 값으로
        if (findPosition == null || findPosition < 0) {
            return
        }

        val currentList = list.value.orEmpty().toMutableList()
        currentList.removeAt(findPosition)
        _list.value = currentList
    }

    fun modifyBookmarkItem(bookmarkModel: BookmarkModel) {
        val findPosition = findIndex(bookmarkModel)
        if (findPosition == null || findPosition <0){
            return
        }
        val currentList = list.value.orEmpty().toMutableList()
        currentList[findPosition] = bookmarkModel
        _list.value = currentList
    }
}