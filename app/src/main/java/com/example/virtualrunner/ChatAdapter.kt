import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.virtualrunner.ChatMessage
import com.example.virtualrunner.R

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(if (viewType == SENT_MESSAGE) R.layout.item_sent_message else R.layout.item_received_message, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = getItem(position)
        holder.messageText.text = message.content
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isSent) SENT_MESSAGE else RECEIVED_MESSAGE
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
    }

    companion object {
        const val SENT_MESSAGE = 1
        const val RECEIVED_MESSAGE = 2
    }

    private class MessageDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.content == newItem.content
        }
    }
}
