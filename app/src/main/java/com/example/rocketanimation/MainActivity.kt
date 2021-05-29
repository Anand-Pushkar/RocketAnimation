package com.example.rocketanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val TAG: String = "AppDebug"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val animationState = remember { mutableStateOf(false) }
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
            ){
                Rocket(
                    isRocketEnabled = animationState.value,
                    maxWidth = maxWidth,
                    maxHeight = maxHeight
                )
                LaunchButton(
                    animationState = animationState.value,
                    onToggleAnimationState = {
                        animationState.value = !animationState.value
                    }
                )
            }
        }
    }
}

@Composable
fun LaunchButton(
    animationState: Boolean,
    onToggleAnimationState: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        if(animationState){
            Button(
                onClick = onToggleAnimationState,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("STOP")
            }
        } else{
            Button(
                onClick = onToggleAnimationState,
            ) {
                Text("LAUNCH")
            }
        }
    }
}

@Composable
fun Rocket(
    isRocketEnabled: Boolean,
    maxWidth: Dp,
    maxHeight: Dp
){
    val resource: Painter
    val modifier: Modifier
    val rocketSize = 200.dp

    if(!isRocketEnabled){
        resource = painterResource(id = R.drawable.rocket_intial)
        modifier = Modifier.offset(
            y = maxHeight - rocketSize
        )
    } else {
        val infiniteTransition = rememberInfiniteTransition()

        // defining the engine flames/state
        val engineState = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 500,
                    easing = LinearEasing
                )
            )
        )
        // defining the position state of the rocket moving across the screen
        val positionState = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
                    easing = LinearEasing
                )
            )
        )

        // this logic animates the flames
        resource = if(engineState.value <= 0.5f){
            painterResource(id = R.drawable.rocket1)
        } else{
            painterResource(id = R.drawable.rocket2)
        }

        // we are using this modifier to change the
        // position of the rocket across the screen
        modifier = Modifier.offset(
            x = (maxWidth - rocketSize) * positionState.value,
            y = (maxHeight - rocketSize) - ((maxHeight - rocketSize) * positionState.value)
        )
    }

    Image(
        modifier = modifier
            .width(rocketSize)
            .height(rocketSize),
        painter = resource,
        contentDescription = "Rocket"
    )
}

