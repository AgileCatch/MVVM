package com.example.mvvmtodolist.todo.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.atomic.AtomicLong

class TodoViewModel : ViewModel() { //비즈니스 로직을 전부 viewModel에서 처리 -> 이후 처리한 데이터를 뷰에다 알려줌

    private val _list: MutableLiveData<List<TodoModel>> = MutableLiveData() // _list : viewModel 내부적으로 control 하는 데이터
    val list: LiveData<List<TodoModel>> get() = _list // 읽기만 가능한 상태

    // id 를 부여할 값
    private val idGenerate = AtomicLong(1L) // Data의 역할이기 때문에 repository(dataLayer)로 옮기는 작업할 예정
    //AtomicLong은 Long 자료형을 갖고 있는 Wrapping 클래스이다.
    //Thread-safe로 구현되어 멀티쓰레드에서 synchronized 없이 사용할 수 있다.
    //또한 synchronized 보다 적은 비용으로 동시성을 보장할 수 있다.

    init {
        _list.value = arrayListOf<TodoModel>().apply {
            for (i in 0 until 3) {
                add(
                    TodoModel(
                        idGenerate.getAndIncrement(),
                        title = "title $i",
                        description = "description $i",
                        isBookmark = false
                    )
                )
            }
        }
    }

    fun addTodoItem(
        todoModel: TodoModel?
    ) {
        if (todoModel == null) {
            return
        }
        val currentList = list.value.orEmpty().toMutableList()
        _list.value = currentList.apply {
            add(
                todoModel.copy( // item이 추가 될 때 id를 생성해서 반영해줌
                    id = idGenerate.getAndIncrement()
                )
            )
        }
    }


    fun removeItem(position: Int?) {
        if (position == null || position < 0) {
            return
        }
        val currentList = list.value.orEmpty().toMutableList()
        currentList?.removeAt(position)
        _list.value = currentList
    }

    fun modifyTodoItem(
        position: Int?,
        todoModel: TodoModel?
    ) {
        fun findIndex(todoModel: TodoModel): Int? {
            val currentList = list.value?.toMutableList()
            val findTodoById = currentList?.find { // 수정하고자 하는 todoModel의 id와 currentList의 id를 비교
                it.id == todoModel.id
            }
            return currentList?.indexOf(findTodoById)//indexOf : 찾고자 하는 Array의 index를 반환
        }

        if (todoModel == null) {
            return
        }

        val findPosition = position ?: findIndex(todoModel) //position이 안들어왔을 경우 id 값으로
        if (findPosition == null || findPosition < 0) {
            return
        }

        val currentList = list.value.orEmpty().toMutableList()
        currentList[findPosition] = todoModel
        _list.value = currentList
    }

}