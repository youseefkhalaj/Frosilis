package com.youeefkhalaj.frosilis.data

import kotlinx.coroutines.flow.Flow

class OfflinePersonalsRepository(private val personalDao: PersonalDao) : PersonalsRepository {
    override fun getAllPersonalsStream(): Flow<List<Personal>> = personalDao.getAllItems()

    override fun getPersonalStream(id: Int): Flow<Personal?> = personalDao.getItem(id)

    override suspend fun insertPersonal(personal: Personal) = personalDao.insert(personal)

    override suspend fun deletePersonal(personal: Personal) = personalDao.delete(personal)

    override suspend fun updatePersonal(personal: Personal) = personalDao.update(personal)
}