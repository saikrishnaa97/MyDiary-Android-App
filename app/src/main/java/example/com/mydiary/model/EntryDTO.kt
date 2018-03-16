package example.com.mydiary.model

import java.util.*

/**
 * Created by saikr on 16-03-2018.
 */
class EntryDTO {

    private var id: String? = null
    private var message: String? = null
    private var title: String? = null
    private var doe: Date? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun getDoe(): Date? {
        return doe
    }

    fun setDoe(doe: Date) {
        this.doe = doe
    }
}