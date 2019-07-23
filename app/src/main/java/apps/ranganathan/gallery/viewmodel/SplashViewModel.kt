package apps.ranganathan.gallery.viewmodel

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.lifecycle.MutableLiveData
import apps.ranganathan.gallery.BuildConfig
import kotlinx.coroutines.Job

class SplashViewModel : BaseViewModel() {


    val version = MutableLiveData<String>()

    init {
        loadAppVersion()
        version.value = "version : ${BuildConfig.VERSION_NAME}"

    }


    private lateinit var request: Job

    private fun loadAppVersion() {


        /* request = GlobalScope.launch {
             val apiResponse = repository.getAppAsync()
             val res = handleResponses(apiResponse!!)
             try {
                 appVersion.postValue(res.data.appVersion)
             } catch (e: Exception) {
                 *//*ignore the exception*//*
            }
        }*/


    }

    override fun onCleared() {

       // request.cancel()
        super.onCleared()
    }

    fun animateUI(view: View) {
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f

        )
        rotateAnimation.duration = 1000
        rotateAnimation.repeatCount = 1

        //Either way you can add Listener like this
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                view.animate().alpha(0.0f).duration = 500


            }
        })
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1.0f).setDuration(1000)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.startAnimation(rotateAnimation)
                }
            })
    }
}
