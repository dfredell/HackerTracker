package com.shortstack.hackertracker.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.shortstack.hackertracker.models.Conference
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Chris on 3/31/2018.
 */
@Dao
interface ConferenceDao {

    @Query("DELETE FROM conference")
    fun deleteAll()

    @Query("SELECT * FROM conference")
    fun getAll(): LiveData<List<Conference>>

    @Query("SELECT * FROM conference")
    fun get(): List<Conference>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(conference: Conference)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(conferences: List<Conference>)


    @Query("SELECT * FROM conference WHERE isSelected = 1")
    fun getCurrentCon(): Conference

    @Update
    fun update(conference: Conference): Int

    @Update
    fun update(list: List<Conference>): Int

    @Query("SELECT * FROM conference where `index` = :id")
    fun getCon(id: Int): Single<Conference>


    @Transaction
    fun upsert(conference: Conference) {
        val updated = update(conference)
        if (updated == 0) {
            insert(conference)
        }
    }
}