package kr.hs.dongpae.tv

import com.google.firebase.database.*


@Suppress("UNCHECKED_CAST")
class FirebaseDatabaseMap<D>(databaseReference: DatabaseReference) {

    private val map = mutableMapOf<String, D>()

    private var updateListener: UpdateListener? = null

    init {
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                map[snapshot.key.toString()] = snapshot.value as D
                updateListener?.onUpdate() // TODO("임시조치")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                map[snapshot.key.toString()] = snapshot.value as D
                updateListener?.onUpdate()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                map.remove(snapshot.key)
                updateListener?.onUpdate()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun get() = map.toMap()

    fun setOnUpdateListener(listener: UpdateListener) {
        updateListener = listener
    }
    fun setOnUpdateListener(listener: () -> Unit) {
        updateListener = object: UpdateListener {
            override fun onUpdate() = listener()
        }
    }

    interface UpdateListener {
        fun onUpdate() {

        }
    }
}