package org.sil.storyproducer.controller.keyterm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import org.sil.storyproducer.R
import org.sil.storyproducer.model.RecordingBacktranslationsPair

class RecordingExpandableListAdapter(val context: Context?, val recordings: List<RecordingBacktranslationsPair>) : BaseExpandableListAdapter(){
    override fun getChild(groupPosition: Int, childPosititon: Int): Any {
        return recordings[groupPosition].backtranslations[childPosititon]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var childView = convertView
        if (childView == null) {
            val infalInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            childView = infalInflater.inflate(R.layout.backtranslation_comment_list_item, null)
        }

        val backtranslationCommentTitle = childView?.findViewById(R.id.backtranslation_comment_title) as TextView
        backtranslationCommentTitle.text = getChild(groupPosition, childPosition) as String

        return childView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return recordings[groupPosition].backtranslations.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return recordings[groupPosition]
    }

    override fun getGroupCount(): Int {
        return recordings.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        var groupView = convertView
        if (groupView == null) {
            val infalInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            groupView = infalInflater.inflate(R.layout.keyterm_audio_comment_list_item, null)
        }

        val audioCommentTitle = groupView?.findViewById(R.id.audio_comment_title) as TextView
        val group = getGroup(groupPosition) as RecordingBacktranslationsPair
        audioCommentTitle.text = group.recording

        return groupView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}