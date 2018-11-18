package org.sil.storyproducer.controller.community

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import org.sil.storyproducer.R
import org.sil.storyproducer.controller.SlidePhaseFrag
import org.sil.storyproducer.controller.ToolbarFrag
import org.sil.storyproducer.controller.adapter.RecordingAdapterV2
import org.sil.storyproducer.model.PhaseType
import org.sil.storyproducer.model.Workspace
import org.sil.storyproducer.model.logging.saveLog
import org.sil.storyproducer.tools.file.deleteStoryFile


/**
 * Fragment for the community check view. The purpose of this phase is for the community to make
 * sure the draft is okay and leave any comments should they feel the need
 */
class CommunityCheckFrag : SlidePhaseFrag() {
    private lateinit var viewManager : RecyclerView.LayoutManager
    private lateinit var viewAdapter : RecyclerView.Adapter<*>
    private var currentPlayingButton: ImageButton? = null
    private var values : MutableList<String>? = null
    private var listener : OnAudioPlayListener? = null

    interface OnAudioPlayListener {
        fun onPlayButtonClicked(path: String, image : ImageButton, stopImage: Int, playImage : Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get names from workspace to populate recyclerView
        values = Workspace.activePhase.getRecordedAudioFiles(slideNum)
        viewManager = LinearLayoutManager(context)
        viewAdapter = RecordingAdapterV2(values)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_community_check, container, false)

        val recyclerView = rootView?.findViewById<RecyclerView>(R.id.recording_list)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = viewAdapter
        recyclerView?.layoutManager = viewManager
        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        (viewAdapter as RecordingAdapterV2).onItemClick = { value ->
            Workspace.activePhase.setChosenFilename(value)
        }
        (viewAdapter as RecordingAdapterV2).onPlayClick = { name, button ->
            onPlayClick(name, button)
        }
        (viewAdapter as RecordingAdapterV2).onDeleteClick = { name ->
            onDeleteClick(name)
        }

        val arguments = Bundle()
        arguments.putBoolean("enablePlaybackButton", false)
        arguments.putBoolean("enableDeleteButton", false)
        arguments.putBoolean("enableMultiRecordButton", false)
        arguments.putBoolean("enableSendAudioButton", false)
        arguments.putInt("slideNum", slideNum)

        val toolbarFrag = childFragmentManager.findFragmentById(R.id.bottom_toolbar) as ToolbarFrag
        toolbarFrag.arguments = arguments
        toolbarFrag.setupToolbarButtons()
        //hook into play button on click to notify adapter to update
        toolbarFrag.view?.findViewById<ImageButton>(0)?.setOnClickListener {
            toolbarFrag.micListener()
            (recyclerView?.adapter)?.notifyDataSetChanged()
        }

        return rootView
    }
    fun onPlayClick(name: String, buttonClickedNow: ImageButton) {
        //if(currentPlayingButton?.background == buttonClickedNow)
        listener?.onPlayButtonClicked(name, currentPlayingButton!!, R.drawable.ic_stop_white_36dp, R.drawable.ic_play_arrow_white_36dp)
        Toast.makeText(context!!, R.string.recording_toolbar_play_back_recording, Toast.LENGTH_SHORT).show()
        when (Workspace.activePhase.phaseType){
            PhaseType.DRAFT -> saveLog(context!!.getString(R.string.DRAFT_PLAYBACK))
            PhaseType.COMMUNITY_CHECK-> saveLog(context!!.getString(R.string.COMMENT_PLAYBACK))
            else -> {}
        }
    }
    fun onDeleteClick(name: String) {
        values?.remove(name)
        deleteStoryFile(context!!, name)
        if(name == Workspace.activePhase.getChosenFilename()){
            if(values!!.size > 0)
                Workspace.activePhase.setChosenFilename(values?.last().toString())
            else{
                Workspace.activePhase.setChosenFilename("")
            }
        }
        val listView = rootView?.findViewById<RecyclerView>(R.id.recording_list)
        (listView?.adapter)?.notifyDataSetChanged()
    }
}
