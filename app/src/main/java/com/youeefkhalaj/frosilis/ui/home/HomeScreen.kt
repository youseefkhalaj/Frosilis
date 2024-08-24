/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.youeefkhalaj.frosilis.ui.home

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.youeefkhalaj.frosilis.FrosilisTopAppBar
import com.youeefkhalaj.frosilis.R
import com.youeefkhalaj.frosilis.data.Personal
import com.youeefkhalaj.frosilis.data.Shift
import com.youeefkhalaj.frosilis.data.calendarData.utils.extensions.toPersianNumber
import com.youeefkhalaj.frosilis.ui.AppViewModelProvider
import com.youeefkhalaj.frosilis.ui.navigation.FrosilisBottomNavigation
import com.youeefkhalaj.frosilis.ui.navigation.NavigationDestination
import com.youeefkhalaj.frosilis.ui.theme.FrosilisTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToHomeOverView: ()-> Unit,
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    navigateToItemeDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val view = LocalView.current
    val window = (view.context as Activity).window


    val insetsController = WindowCompat.getInsetsController(window, view)
    insetsController.apply{
        show(WindowInsetsCompat.Type.systemBars())

    }

    val activty = LocalContext.current as Activity

    activty.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            FrosilisBottomNavigation(
                navigateOverviewHome = {navigateToHomeOverView()},
                navigateHome = {}
            )},
        topBar = {
            FrosilisTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        }){ innerPadding ->

        HomeBody(

            personalList =homeUiState.personalItemList,
            onItemClick = navigateToItemUpdate,
            onEditClicked = navigateToItemeDetail ,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun HomeBody(
    personalList: List<Personal>,
    onItemClick: (Int) -> Unit,
    onEditClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues ,
) {





    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (personalList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            FrosilisList(
                personalList = personalList,
                onItemClick = { onItemClick(it.id) },
                onEditClicked = {onEditClicked(it.id)},
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun FrosilisList(
    personalList: List<Personal>,
    onItemClick: (Personal) -> Unit,
    onEditClicked: (Personal) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = personalList, key = { it.id }) { personalItem ->
            PersonalItem(personal = personalItem,
                clicked = { onEditClicked(personalItem) },
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(personalItem) })
        }
    }
}

@Composable
private fun PersonalItem(
    clicked: () -> Unit,
    personal:Personal,
    modifier: Modifier = Modifier
) {
    var extended by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(2.dp),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {


                    if (personal.image == null) {
                        Image(
                            painter = painterResource(id = R.drawable.blank_profile_picture_973460_1920),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .border(1.dp, MaterialTheme.colorScheme.scrim, CircleShape)
                                .padding(4.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            bitmap = personal.image.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .border(1.dp, MaterialTheme.colorScheme.scrim, CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    }
                    Spacer(modifier = modifier.width(4.dp))
                    Text(
                        text = personal.name,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(text = personal.shift.toString())
                    Spacer(modifier = Modifier.weight(1f))

                    Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)) {
                        Text(text = (personal.overTimeAddCurrentMonth + personal.currentMonthOverTime).toPersianNumber())
                    }

                    Text(text = "=",modifier = Modifier.padding(start = 3.dp, end = 3.dp))

                    Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer)) {
                        Text(text = personal.overTimeAddCurrentMonth.toPersianNumber())
                    }

                    Text(text = "+",modifier = Modifier.padding(start = 3.dp, end = 3.dp))
                    Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer)) {
                        Text(text = personal.currentMonthOverTime.toPersianNumber())
                    }



                    ItemIconBottom(clicked = { extended = !extended }, extended = extended)
                }

                if (extended) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Row {
                       }
                        Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "ویرایش",
                            modifier = Modifier.clickable { clicked() })
                    }


                    }
                }

            }

            }
        }

    @Composable
    fun ItemIconBottom(
        clicked: () -> Unit,
        extended: Boolean,
        modifier: Modifier = Modifier
    ) {
        Modifier
        IconButton(onClick = clicked) {
            Icon(
                imageVector = if (extended) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null,
                modifier = modifier,
                tint = MaterialTheme.colorScheme.scrim
            )
        }
    }




@Preview(showBackground = true, locale = "fa", showSystemUi = true)
@Composable
fun InventoryItemPreview() {
    FrosilisTheme {
        PersonalItem(
            clicked = {},
            Personal(1, "جواد سالاروند", null, currentMonthOverTime = 33, overTimeAddCurrentMonth = 0,Array(10) { Array(12) { IntArray(31) { 0 } } }, shift = Shift.A)

            )
    }
    }


