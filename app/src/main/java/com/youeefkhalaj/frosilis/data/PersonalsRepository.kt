package com.youeefkhalaj.frosilis.data

import kotlinx.coroutines.flow.Flow

interface PersonalsRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllPersonalsStream(): Flow<List<Personal>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getPersonalStream(id: Int): Flow<Personal?>

    /**
     * Insert item in the data source
     */
    suspend fun insertPersonal(personal: Personal)

    /**
     * Delete item from the data source
     */
    suspend fun deletePersonal(personal: Personal)

    /**
     * Update item in the data source
     */
    suspend fun updatePersonal(personal: Personal)}