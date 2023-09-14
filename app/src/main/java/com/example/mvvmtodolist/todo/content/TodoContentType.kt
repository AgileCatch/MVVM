package com.example.mvvmtodolist.todo.content

enum class TodoContentType {
    ADD, EDIT, REMOVE;

    companion object {
        fun from(name: String?): TodoContentType? {
            return TodoContentType.values().find {
                it.name.uppercase() == name?.uppercase()
            }
        }
    }
}