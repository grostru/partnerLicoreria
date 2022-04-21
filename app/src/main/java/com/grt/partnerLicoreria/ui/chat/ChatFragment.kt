package com.grt.partnerLicoreria.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.grt.partnerLicoreria.R
import com.grt.partnerLicoreria.data.Constants
import com.grt.partnerLicoreria.common.BaseFragment
import com.grt.partnerLicoreria.data.firestore.MessageModelDB
import com.grt.partnerLicoreria.databinding.FragmentChatBinding
import com.grt.partnerLicoreria.domain.model.MessageModel
import com.grt.partnerLicoreria.domain.model.OrderModel
import com.grt.partnerLicoreria.ui.track.TrackFragmentArgs
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(), OnChatListener {

    override val vm: ChatViewModel by sharedViewModel()

    private lateinit var adapter: ChatAdapter

    private var order: OrderModel? = null

    val args: TrackFragmentArgs by navArgs()

    override fun provideBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getOrder()

        setupRecyclerView()
        setupButtons()
    }

    private fun getOrder() {
        order = args.orderSelected

        order?.let {
            setupActionBar()
            setupRealtimeDatabase()
        }
    }

    private fun setupRealtimeDatabase() {

        order?.let {
            val database = FirebaseDatabase.getInstance(Constants.URL_REALTIME_DATABASE)
            val chatRef = database.getReference(Constants.PATH_CHATS).child(it.id)
            val childListener = object  : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    getMessage(snapshot)?.let {
                        adapter.add(it)
                        binding?.recyclerView?.scrollToPosition(adapter.itemCount - 1)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    getMessage(snapshot)?.let {
                        adapter.update(it)
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    /*val message = snapshot.getValue(Message::class.java)
                    message?.let { message ->
                        snapshot.key?.let {
                            message.id = it
                        }
                        FirebaseAuth.getInstance().currentUser?.let { user ->
                            message.myUid = user.uid
                        }
                        adapter.delete(message)
                    }*/
                    getMessage(snapshot)?.let {
                        adapter.delete(it)
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {
                    binding?.let {
                        Snackbar.make(it.root, "Error al cargar chat.", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
            chatRef.addChildEventListener(childListener)
        }
    }

    private fun getMessage(snapshot: DataSnapshot): MessageModel? {
        snapshot.getValue(MessageModel::class.java)?.let { message ->
            snapshot.key?.let {
                message.id = it
            }
            FirebaseAuth.getInstance().currentUser?.let { user ->
                message.myUid = user.uid
            }
            return  message
        }
        return null
    }

    private fun setupRecyclerView(){
        adapter = ChatAdapter(mutableListOf(), this)
        binding?.let {
            it.recyclerView.apply {
                layoutManager = LinearLayoutManager(context).also {
                    it.stackFromEnd = true
                }
                adapter = this@ChatFragment.adapter
            }
        }

        /*(1..20).forEach {
            adapter.add(MessageModelDB(it.toString(), if(it%4 == 0)"Hola, ¿Como estas?, Hola, ¿Como estas?, Hola, ¿Como estas?" else "Hola, ¿Como estas?",
                if(it%3 == 0) "tu" else "yo", "yo"))
        }*/
    }

    private fun setupButtons(){
        binding?.let { binding ->
            binding.ibSend.setOnClickListener {
                sendMessage()
            }
        }
    }

    private fun sendMessage() {
        binding?.let { binding ->
            order?.let {
                val database = FirebaseDatabase.getInstance(Constants.URL_REALTIME_DATABASE)
                val chatRef = database.getReference(Constants.PATH_CHATS).child(it.id)
                val user = FirebaseAuth.getInstance().currentUser

                user?.let {
                    val message = MessageModelDB(message = binding.etMessage.text.toString().trim(),
                        sender = it.uid)

                    binding.ibSend.isEnabled = false

                    chatRef.push().setValue(message)
                        .addOnSuccessListener {
                            binding.etMessage.setText("")
                        }
                        .addOnCompleteListener {
                            binding.ibSend.isEnabled = true
                        }
                }
            }
        }
    }

    private fun setupActionBar(){
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar?.title = getString(R.string.chat_title)
            setHasOptionsMenu(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            it.supportActionBar?.title = getString(R.string.order_title)
            setHasOptionsMenu(false)
        }
        super.onDestroy()
    }

    override fun deleteMessage(messageModel: MessageModel) {
        order?.let {
            val database = Firebase.database
            val messageRef = database.getReference(Constants.PATH_CHATS).child(it.id).child(messageModel.id)
            messageRef.removeValue { error, ref ->
                binding?.let {
                    if (error != null){
                        Snackbar.make(it.root, "Error borrar mensaje.", Snackbar.LENGTH_LONG).show()
                    } else {
                        Snackbar.make(it.root, "Mensaje borrado.", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}