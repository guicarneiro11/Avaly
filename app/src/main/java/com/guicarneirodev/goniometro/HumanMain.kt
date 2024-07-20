package com.guicarneirodev.goniometro

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.biodigital.humansdk.*

class HumanMain : AppCompatActivity(), HKServicesInterface, HKHumanInterface {
    private lateinit var human: HKHuman

    companion object {
        const val MODEL_MESSAGE = "com.guicarneirodev.MODEL_MESSAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val modelId = intent.getStringExtra(MODEL_MESSAGE)
        HKServices.getInstance().setup(applicationContext, this)

        modelId?.let {
            loadModel(it)
        }
    }

    private fun loadModel(modelId: String) {
        val rl = findViewById<RelativeLayout>(R.id.humanBody)
        val uimap = HashMap<HumanUIOptions, Boolean>()
        uimap[HumanUIOptions.all] = true
        human = HKHuman(rl, uimap)
        runOnUiThread {
            human.load(modelId)
        }
    }

    override fun onValidSDK() {

    }

    override fun onInvalidSDK() {

    }

    override fun onModelsLoaded() {

    }

    override fun onModelDownloaded(modelId: String?, count: Int?, total: Int?) {

    }

    override fun onModelDownloadError(modelId: String?) {

    }

    override fun onAnimationComplete() {

    }

    override fun onAnnotationCreated(annotationId: String?) {

    }

    override fun onAnnotationDestroyed(annotationId: String?) {

    }

    override fun onAnnotationsShown(isShown: Boolean?) {

    }

    override fun onAnnotationUpdated(annotation: HKAnnotation?) {

    }

    override fun onCameraUpdated(camera: HKCamera?) {

    }

    override fun onChapterTransition(chapterId: String?) {

    }

    override fun onModelLoaded(p0: String?) {

    }

    override fun onModelLoadError(modelId: String?) {

    }

    override fun onObjectColor(objectId: String?, color: HKColor?) {

    }

    override fun onObjectDeselected(objectId: String?) {

    }

    override fun onObjectPicked(objectId: String?, position: DoubleArray?) {

    }

    override fun onObjectSelected(objectId: String?) {

    }

    override fun onObjectsShown(p0: MutableMap<String, Any>?) {

    }

    override fun onSceneCapture(captureString: String?) {

    }

    override fun onSceneInit(title: String?) {

    }

    override fun onSceneRestore() {

    }

    override fun onScreenshot(image: Bitmap?) {

    }

    override fun onTimelineUpdated(timeline: HKTimeline?) {

    }

    override fun onXrayEnabled(isEnabled: Boolean?) {

    }
}