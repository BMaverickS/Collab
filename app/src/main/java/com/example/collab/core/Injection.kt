package com.example.collab.core

import com.example.collab.core.data.RemoteDataSource

object Injection {
    fun userSourceRepository() : DataSourceRepository {
        val remoteDataSource = RemoteDataSource()

        return DataSourceRepository.getInstance(remoteDataSource)
    }
}

// dependency injection : use another class without knowing how the class is made
// dependency injection can make code loosely coupled (less dependant) and highly cohesion (focus on 1 job)