package com.youeefkhalaj.frosilis.ui.home

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.annotation.ExperimentalTestApi
import com.youeefkhalaj.frosilis.R
import com.youeefkhalaj.frosilis.data.Personal
import com.youeefkhalaj.frosilis.data.calendarData.pojo.CalendarModel
import com.youeefkhalaj.frosilis.data.calendarData.utils.EMPTY_DATE
import com.youeefkhalaj.frosilis.data.calendarData.utils.EMPTY_OVERTIME
import com.youeefkhalaj.frosilis.data.calendarData.utils.REQUEST_REJECTION
import com.youeefkhalaj.frosilis.data.calendarData.utils.extensions.toPersianNumber
import com.youeefkhalaj.frosilis.ui.AppViewModelProvider
import com.youeefkhalaj.frosilis.ui.navigation.FrosilisBottomNavigation
import com.youeefkhalaj.frosilis.ui.navigation.NavigationDestination

object HomeOverviewDestination : NavigationDestination {
    override val route = "homeOverview"
    override val titleRes = R.string.app_name
}


@Composable
fun HomeOverviewScreen(
    modifier: Modifier = Modifier,
    navigateToHome: ()-> Unit,
    overviewViewModel: HomeOverviewViewModel = viewModel(factory = AppViewModelProvider.Factory),

) {
    val activty = LocalContext.current as Activity
    activty.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE






   // val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier,
        bottomBar = {
            FrosilisBottomNavigation(
                navigateOverviewHome = { },
                navigateHome = {navigateToHome()}
                ) },
      ){ innerPadding ->

      CalendarOverviewScreenShow(
          overviewViewModel = overviewViewModel,
          contentPadding = innerPadding)
    }
}


@OptIn(ExperimentalTestApi::class)
@Composable
fun CalendarOverviewScreenShow(
    overviewViewModel: HomeOverviewViewModel,
    contentPadding: PaddingValues = PaddingValues(1.dp)
){

    val calendarUiState by overviewViewModel.uiState.collectAsState()
    val homeOverviewUiState by overviewViewModel.homeOverviewUiState.collectAsState()
    val view = LocalView.current
    val window = (view.context as Activity).window


    val insetsController = WindowCompat.getInsetsController(window, view)
    insetsController.apply{
        hide(WindowInsetsCompat.Type.systemBars())
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }


    Column(horizontalAlignment = Alignment.Start) {
        ListMontGrid(listMont = calendarUiState.listMonthCurrent,
            )
        HorizontalDivider(modifier = Modifier.padding(5.dp), Dp(1f) , color = MaterialTheme.colorScheme.primary)
        PersonalListOverview(
            personalList = homeOverviewUiState.personalItemList,
           contentPadding = contentPadding,overviewViewModel = overviewViewModel
        )
    }


}


@Composable
private fun PersonalListOverview(
    overviewViewModel: HomeOverviewViewModel,
    personalList: List<Personal>,
    contentPadding: PaddingValues,
) {

    var random = false
    LazyColumn(

        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        contentPadding = contentPadding
    ) {
        items(items = personalList) { personalItem ->
            PersonalItemOverView(
                personal = personalItem,
                overviewViewModel = overviewViewModel,
                randomRow = random,
                changeRandom = {random = !random}

            )

        }

    }
}



@Composable
private fun PersonalItemOverView(
    overviewViewModel: HomeOverviewViewModel,
    randomRow: Boolean,
    changeRandom: ()->Unit,
    personal: Personal,
    modifier: Modifier = Modifier
){
   val listOverTimeForDay = overviewViewModel.listOverTime(personal.horseOvertime)
    Row (horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 5.dp)){
        Text(text = personal.name,
        modifier = Modifier.size(width = 95.dp, height = 20.dp))
        listOverTimeForDay.forEach {
            if (it != EMPTY_DATE){
                ItemOverTimeOverview(
                    randomRow = randomRow,
                    changeRandom =changeRandom,
                    overTime = it)
            }
        }
        Spacer(modifier = Modifier.width(2.dp))
        Card {
            Text(
                text = personal.currentMonthOverTime.toPersianNumber(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .size(width = 25.dp, height = 20.dp)
                    .fillMaxWidth())
        }




    }
    HorizontalDivider(modifier = Modifier.padding(.1.dp), Dp(.3f) , color = MaterialTheme.colorScheme.primary)
}


@Composable
private fun ItemOverTimeOverview(
    randomRow: Boolean,
    changeRandom: ()->Unit,
    overTime: Int){
    val text = when(overTime){
        0 -> ""
        REQUEST_REJECTION -> "*"
        EMPTY_OVERTIME -> "-"
        else -> overTime.toPersianNumber()

    }

Card (
    modifier = Modifier.size(width = 20.dp, height = 20.dp),
    colors =if (randomRow) CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer) else
        CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
){
    Text(
        text = text,
        textAlign = TextAlign.Center,
       // fontSize = 10.sp
        modifier = Modifier.fillMaxWidth()
    )
    changeRandom()

}
}





@Composable
fun ListMontGrid(listMont : List<CalendarModel>){

    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(start = 100.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)){
        listMont.forEach{
            if(it.iranianDay != EMPTY_DATE) {
            WeekDayMonthCard(montWeek = it.dayOfWeek, montDay = it.iranianDay.toPersianNumber(), holiday = it.isHolidayOccasion)
                }
        }
    }
}



@Composable
fun WeekDayMonthCard(
    montWeek: Int,
    montDay: String,
    holiday: Boolean,
){
    val weekDay = when (montWeek){
        0 -> R.string.mondayy
        1 -> R.string.tuesdayy
        2 -> R.string.wednesdayss
        3 -> R.string.thursdayy
        4 -> R.string.fridayy
        5 -> R.string.saturdayy
        else -> R.string.sundayy
    }
    val colorCard: Color

        if (montWeek == 4) {
            colorCard = MaterialTheme.colorScheme.errorContainer
        }else if (holiday){
            colorCard = MaterialTheme.colorScheme.error
        }else{colorCard =  MaterialTheme.colorScheme.surfaceContainer}

    Card (colors = CardDefaults.cardColors(containerColor = colorCard ),
        modifier = Modifier.size(width = 20.dp, height = 50.dp)){
        Box {
            Text(text = "A \n b",
                fontSize = 6.sp)
        Column (){
            Text(
                text = stringResource(id = weekDay),
                textAlign = TextAlign.Center,
                fontSize = 9.sp,
                modifier = Modifier.fillMaxWidth(),

            )


            Text(text = montDay,
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}




@Preview(showSystemUi = true)
@Composable
fun OverViewPreview(){
 //  WeekDayMonthCard(montWeek = "جمعه")
}