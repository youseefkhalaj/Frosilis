package com.youeefkhalaj.frosilis.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youeefkhalaj.frosilis.R
import com.youeefkhalaj.frosilis.ui.theme.FrosilisTheme

@Composable
fun FrosilisBottomNavigation(
    navigateHome: ()-> Unit,
    navigateOverviewHome: ( )-> Unit,
    modifier: Modifier = Modifier){
    NavigationBar(modifier = modifier.height(50.dp),
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { navigateHome() },
            icon = { Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null
            )}
//            ,
//            label = {
//                Text(text = stringResource( R.string.bottom_navigation_home))
//            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navigateOverviewHome() },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null)
            },
//            label = {
//                Text(stringResource(R.string.bottom_navigation_profile))
//            }
        )

    }
}

@Composable
fun FrosilisNavigationRail(modifier: Modifier = Modifier){
    NavigationRail(modifier = modifier.padding(start = 8.dp , end = 8.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            NavigationRailItem(
                selected = true,
                onClick = {  },
                icon = { Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
                }
                ,
                label = {
                    Text(text = stringResource( R.string.bottom_navigation_home))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            NavigationRailItem(
                selected = false,
                onClick = { },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null)
                },
                label = {
                    Text(stringResource(R.string.bottom_navigation_profile))
                }
            )

        }

    }
}

@Preview
@Composable
fun NavigationBarPreview(){
    FrosilisTheme {
        FrosilisBottomNavigation({},{})

    }



}
@Preview
@Composable
fun NavigationRailPreview(){
    FrosilisTheme{
        FrosilisNavigationRail()
    }



}