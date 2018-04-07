package example.com.mydiary.view

/**
 * Created by saikr on 01-04-2018.
 */
interface IHomeActivityCommunicator {

    fun delete(entryId : String)
    fun openFullEntry(id: String?)

}