package com.example.mvvmtodolist.todo.home

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmtodolist.databinding.TodoFragmentBinding
import com.example.mvvmtodolist.main.MainActivity
import com.example.mvvmtodolist.todo.content.TodoContentActivity
import com.example.mvvmtodolist.todo.content.TodoContentType

class TodoFragment : Fragment() {

    companion object {
        fun newInstance() = TodoFragment()
    }

    private var _binding: TodoFragmentBinding? = null
    private val binding get() = _binding!!

//    AAC viewModel을 선언할 때 다른얘들과는 다르게 provider를 통해 선언해주어야한다.
    private val viewModel : TodoViewModel by lazy {
        ViewModelProvider(this).get(TodoViewModel::class.java)
    }


    private val editTodoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val entryType =
                    result.data?.getStringExtra(TodoContentActivity.EXTRA_TODO_ENTRY_TYPE)
                val position = result.data?.getIntExtra(TodoContentActivity.EXTRA_TODO_POSITION, -1)
                val todoModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(
                        TodoContentActivity.EXTRA_TODO_MODEL,
                        TodoModel::class.java
                    )
                } else {
                    result.data?.getParcelableExtra(
                        TodoContentActivity.EXTRA_TODO_MODEL
                    )
                }

                // entry type 에 따라 기능 분리
                when (TodoContentType.from(entryType)) {
                    TodoContentType.EDIT -> modifyTodoItem(item=todoModel, position)
                    TodoContentType.REMOVE -> removeItemTodoItem(position)
                    else -> Unit // nothing
                }
            }
        }

    private val listAdapter by lazy {
        TodoListAdapter(
            onClickItem = { position, item ->
                editTodoLauncher.launch(
                    TodoContentActivity.newIntentForEdit(
                        requireContext(),
                        position,
                        item
                    )
                )
            },
            onBookmarkChecked = { position, item ->
                if (item.isBookmark){
                    addItemToBookmarkTab(item) // BookMarkTap에 Item 추가
                }else{
                    removeItemToBookmarkTab(item)
                }
                modifyTodoItem(item,position ) // BookMark가 check됐을 때 Item수정

            }
        )

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TodoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel() // viewModel
    }

    private fun initModel() = with(viewModel) { // viewModel
        // viewModel 상 읽기용 list
        list.observe(viewLifecycleOwner) { // Fragment LV : observe(viewLifecycleOwner)
            listAdapter.submitList(it)
        }
    }

    private fun initView() = with(binding) {
        todoList.adapter = listAdapter
    }

    fun setDodoContent(todoModel: TodoModel?) {
        viewModel.addTodoItem(todoModel)
    }

    fun modifyTodoItem(
        item: TodoModel?,
        position: Int? = null
    ) {
        viewModel.modifyTodoItem(position, item)
    }

    /** 아이템을 삭제합니다.*/
    private fun removeItemTodoItem(position: Int?) {
        viewModel.removeItem(position)
    }

    /** Bookmark Tab 에 아이템을 추가합니다.*/
    private fun addItemToBookmarkTab(item: TodoModel) {
        (activity as MainActivity).addBookmarkItem(item) //다음의 통해 진행하면 메모리 누수, 생명주기..? 등 다양한 문제가 발생할 수 있다.
        // -> 바로 MainActivity 뷰모델에 접근하는게 좋다.
        //MainViewModel.addBookmarkItem이런식
        // 형 변환을 통해 현재 호스팅이 MainActivity라는 것을 확신
    }

    private fun removeItemToBookmarkTab(item: TodoModel) {
        (activity as MainActivity).removeBookmarkItem(item)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}