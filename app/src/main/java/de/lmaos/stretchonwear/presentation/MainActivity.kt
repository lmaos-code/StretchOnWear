/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package de.lmaos.stretchonwear.presentation
// add the following imports

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyColumnDefaults.snapFlingBehavior
import androidx.wear.compose.material.ScalingLazyListAnchorType
import androidx.wear.compose.material.ScalingLazyListItemScope
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import de.lmaos.stretchonwear.R
import de.lmaos.stretchonwear.presentation.theme.StretchOnWearTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScalingListView(scalingListState: ScalingLazyListState) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    ScalingLazyColumn(
        state = scalingListState,
        anchorType = ScalingLazyListAnchorType.ItemCenter,
        modifier = Modifier
            .fillMaxSize()
            .onRotaryScrollEvent {
                if (!scalingListState.isScrollInProgress)
                    coroutineScope.launch {
                        var toScrollTo = if(it.verticalScrollPixels <= 0) scalingListState.centerItemIndex + 1 else scalingListState.centerItemIndex - 1
                        if(toScrollTo < 0) toScrollTo = 0
                        if(toScrollTo > scalingListState.layoutInfo.totalItemsCount) toScrollTo = scalingListState.layoutInfo.totalItemsCount
                        scalingListState.animateScrollToItem( toScrollTo
                        )
                        Log.d("ScalingListView", "scrollBy: ${it.verticalScrollPixels} , scrolled to item ${scalingListState.centerItemIndex}")
                    }
                true
            }
            .focusRequester(focusRequester)
            .focusable(),
        contentPadding = PaddingValues(
            top = 10.dp,
            start = 10.dp,
            end = 10.dp,
            bottom = 40.dp
        ),
        flingBehavior = snapFlingBehavior(scalingListState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            SetupScreen(
                displayString = LocalContext.current.getString(R.string.active_seconds),
                staringNumber = 30
            )
        }
        item {
            SetupScreen(displayString = LocalContext.current.getString(R.string.passive_seconds))
        }
        item {
            //Start Button
            Box(
                modifier = Modifier
                    .height(screenHeight)
                    .width(screenWidth)
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = "Start",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                         Log.d("ScalingListView", "Start Button Clicked")
                            //Intent k = new Intent(MainActivity.this, StretchActivity.class);
                              },
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Start",
                        tint = MaterialTheme.colors.onSecondary
                    )
                }
            }
        }


    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        // I don't know why this is needed, but without it the list starts at item[1]
        scalingListState.scrollToItem(0)
    }

}


@Composable
fun ScalingLazyListItemScope.SetupScreen(displayString: String, staringNumber: Int = 5) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var counter by remember { mutableStateOf(staringNumber) }

    Box(
        modifier = Modifier
            .height(screenHeight)
            .width(screenWidth)
    ) {
        Text(
            text = displayString,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )
        Text(
            text = "$counter",
            textAlign = TextAlign.Center,

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = screenHeight / 2 - (SpToDp(
                        MaterialTheme.typography.display1.lineHeight.value,
                        LocalContext.current
                    ) / 2).dp,

                    ),
            style = MaterialTheme.typography.display1
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CompactButton(
                onClick = { counter -= if (counter == 5) 0 else 5; }
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Subtract Icon")
            }
            CompactButton(
                onClick = { counter += 5 }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
            }
        }
    }
}

//dp - sp helpers
fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun dpToSp(dp: Float, context: Context): Int {
    return (dpToPx(dp, context) / context.resources.displayMetrics.scaledDensity).toInt()
}

fun SpToDp(sp: Float, context: Context): Int {
    return PxToDP(SpToPx(sp, context).toFloat(), context)
}

fun PxToDP(px: Float, context: Context): Int {
    return (px / context.resources.displayMetrics.density).toInt()
}

fun SpToPx(sp: Float, context: Context): Int {
    return (sp * context.resources.displayMetrics.scaledDensity).toInt()
}

@Composable
fun WearApp() {
    StretchOnWearTheme {
        val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
            timeText = { TimeText() },
            positionIndicator = { PositionIndicator(scalingLazyListState) },
            content = {
                ScalingListView(scalingLazyListState)
            }
        )

    }
}


@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
@Composable
fun StretchOnWear() {
    WearApp()
}