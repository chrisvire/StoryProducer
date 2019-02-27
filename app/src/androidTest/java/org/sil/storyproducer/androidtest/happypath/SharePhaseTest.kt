package org.sil.storyproducer.androidtest.happypath

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.sil.storyproducer.androidtest.utilities.Constants
import org.sil.storyproducer.androidtest.utilities.PhaseNavigator
import org.sil.storyproducer.model.Workspace
import java.io.File

@LargeTest
@RunWith(AndroidJUnit4::class)
class SharePhaseTest : PhaseTestBase() {
    override fun navigateToPhase() {
        PhaseNavigator.navigateFromRegistrationScreenToPhase(Constants.Phase.share)
    }

    @Test
    fun when_thereAreNoExportedVideos_should_showNoVideosMessage() {
        approveSlides()

        Espresso.onView(withText(org.sil.storyproducer.R.string.no_videos)).check(matches(isDisplayed()))
    }

    @Test
    fun when_aVideoHasBeenExported_should_showItInTheList() {
        approveSlides()
        val videoFilename = Constants.nameOfSampleExportVideo
        selectPhase(Constants.Phase.finalize)
        copySampleVideoToExportDirectory(videoFilename)
        Workspace.activeStory.addVideo(videoFilename)
        selectPhase(Constants.Phase.share)

        Espresso.onView(withText(CoreMatchers.containsString(videoFilename))).check(matches(isDisplayed()))
    }

    private fun copySampleVideoToExportDirectory(videoFilename: String) {
        try {
            val sampleVideo = File(Constants.espressoResourceDirectory + File.separator + videoFilename)
            val destination = File(Constants.exportedVideosDirectory + File.separator + videoFilename)
            sampleVideo.copyRecursively(destination, true)
        } catch (e: Exception){
            Assert.fail("Failed to copy sample video to exported videos directory for test.")
        }
    }

    //TODO: Move this functionality to a base class for all phases that require consultant authorization
    private fun approveSlides() {
        selectPhase(Constants.Phase.accuracyCheck)
        for (item in Workspace.activeStory.slides) {
            item.isChecked = true
        }
        Workspace.activeStory.isApproved = true
        selectPhase(Constants.Phase.share)
    }
}