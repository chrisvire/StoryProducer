package org.sil.storyproducer.model

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FrontCoverContentTest {

    @Test
    fun testDefaultValues() {
        val content = ""

        val frontCoverContent = FrontCoverContent(content, "Story Title", "Scripture Reference")
        Assert.assertEquals("gray-background", frontCoverContent.graphic)
        Assert.assertEquals("Scripture Reference", frontCoverContent.scriptureReference)
        Assert.assertEquals("Title ideas:", frontCoverContent.titleIdeasHeading)
        Assert.assertEquals("Story Title", frontCoverContent.titleIdeas.firstOrNull())
    }

}