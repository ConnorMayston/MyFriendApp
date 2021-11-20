package com.monkeyandriver.myfriendapp.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table
data class User(
    @Column(unique = true, nullable = false)
    val username:String,

    @Column(nullable = false)
    val password:String,

    @Column
    val firstName:String,

    @Column
    val lastName:String
){
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(length = 36, updatable = false, nullable = false)
    val id:String = ""

    @ManyToMany
    @JoinTable
    private val friendList:MutableList<User> = mutableListOf()

    fun getFriendsList():List<User>{
        return friendList.toList()
    }

    fun addFriend(friend:User){
        friendList.add(friend)
    }

    fun removeFriend(friend:User){
        friendList.remove(friend)
    }
}
