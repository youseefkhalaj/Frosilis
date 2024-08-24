@file:Suppress("NAME_SHADOWING")

package com.youeefkhalaj.frosilis.ui.calendar

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.youeefkhalaj.frosilis.FrosilisTopAppBar
import com.youeefkhalaj.frosilis.R
import com.youeefkhalaj.frosilis.data.calendarData.pojo.CalendarModel
import com.youeefkhalaj.frosilis.data.calendarData.utils.EMPTY_DATE
import com.youeefkhalaj.frosilis.data.calendarData.utils.EMPTY_OVERTIME
import com.youeefkhalaj.frosilis.data.calendarData.utils.REQUEST_REJECTION
import com.youeefkhalaj.frosilis.data.calendarData.utils.extensions.toPersianMonth
import com.youeefkhalaj.frosilis.data.calendarData.utils.extensions.toPersianNumber
import com.youeefkhalaj.frosilis.ui.AppViewModelProvider
import com.youeefkhalaj.frosilis.ui.item.PersonalDetails
import com.youeefkhalaj.frosilis.ui.item.PersonalEditDestination
import com.youeefkhalaj.frosilis.ui.navigation.NavigationDestination


object CalendarEditDestination : NavigationDestination {
    override val route = "personal_save_overtime"
    override val titleRes = R.string.personal_save_overtime_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    calendarViewModel: PersonalOverTimeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val calendarPersonalUiState = calendarViewModel.calendarPersonalUiState.collectAsState()

    Scaffold(
        topBar = {
            FrosilisTopAppBar(
                title = stringResource(PersonalEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->

       CalendarScreenShow(
           personalCalendarDetailsUiState = calendarPersonalUiState.value,
           back = {
                   navigateBack()

           },
       personalOverTimeViewModel =calendarViewModel,
           modifier = Modifier.padding(
       innerPadding)
       )


    }
}







@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CalendarScreenShow(
    personalCalendarDetailsUiState: PersonalCalendarDetailsUiState,
    back : () -> Unit,
    personalOverTimeViewModel: PersonalOverTimeViewModel,
    modifier: Modifier = Modifier
)


    {



        var showDialogSelectOverTime by remember {
            mutableStateOf(false)
        }
        var showTimePickerAddOverTime by remember {
            mutableStateOf(false)
        }

       val calendarUiState by personalOverTimeViewModel.uiState.collectAsState()
        personalOverTimeViewModel.addOverTimeToListDay(personalCalendarDetailsUiState.personalDetails.horseOvertime)
       // personalOverTimeViewModel.overTimeForCurrentMonth(personalCalendarDetailsUiState.personalDetails.horseOvertime)




        var year by remember { mutableIntStateOf(0) }
        var month by remember { mutableIntStateOf(0) }
        var day by remember { mutableIntStateOf(0) }

        Column (modifier = modifier){
            TitleCalendar(
                nextMonth = {personalOverTimeViewModel.nextMonth()},
                previousMonth = {personalOverTimeViewModel.previousMonth()} ,
                listMonth =  calendarUiState.listMonthCurrent
            )
            HorizontalDivider(modifier = Modifier.padding(1.dp), Dp(1f) , color = MaterialTheme.colorScheme.primary)
            CalendarHeaderWeek()
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.padding(1.dp), Dp(1f) , color = MaterialTheme.colorScheme.primary)

            CalendarScreenList(
               listMonth =  calendarUiState.listMonthCurrent,
                onItemClick = { yy, mm, dd ->
                    year = yy
                    month = mm
                    day = dd
                    if (yy > 0){
                        showDialogSelectOverTime = true
                    }
                }

            )
            HorizontalDivider(modifier = Modifier.padding(1.dp), Dp(1f) , color = MaterialTheme.colorScheme.primary)
            PersonalDetailsScreen(
                personalDetails = personalCalendarDetailsUiState.personalDetails,

                )

            Spacer(modifier = Modifier.padding(vertical = 30.dp))
            Row (horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()){
                TextButton(onClick = { showTimePickerAddOverTime = true }) {
                    Text(text = "ثبت اضافه کار مازاد")
                }
            }
            Row (horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()){
                TextButton(onClick = {back()}) {
                    Text(text = stringResource(id = R.string.back_button))
                }
            }

                if (showDialogSelectOverTime){

                    DialogGetOverTimeHours(
                        onclickDialog = {
                            personalOverTimeViewModel.overTimeForCurrentMonth(personalCalendarDetailsUiState.personalDetails.horseOvertime)
                            personalOverTimeViewModel.addOverTimeToListDay(personalCalendarDetailsUiState.personalDetails.horseOvertime)
                            showDialogSelectOverTime = false},
                        personalOverTimeViewModel = personalOverTimeViewModel,
                        year = year,
                        month = month,
                        day = day)
                

               }


            if (showTimePickerAddOverTime){
             DialogGetExtraOverTimeHours(
                 onclickDialog = { showTimePickerAddOverTime = false },
                 personalOverTimeViewModel = personalOverTimeViewModel,
             )

            }



    }


}
@Composable
fun DialogGetExtraOverTimeHours(
    onclickDialog : () ->  Unit,
    personalOverTimeViewModel: PersonalOverTimeViewModel,
){

    var hoursOverTimeInput by remember { mutableStateOf("") }

    Dialog(onDismissRequest = {onclickDialog()}) {

        Card (){
            Column(modifier = Modifier.padding(50.dp)) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                }
                Spacer(modifier = Modifier.padding(horizontal = 1.dp))

                Row (modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){

                    Text(text = stringResource(id = R.string.InterHour),
                        modifier = Modifier.padding(start = 5.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    OutlinedTextField(
                        value = hoursOverTimeInput,
                        onValueChange = { hoursOverTimeInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                        modifier = Modifier.size(width = 60.dp, height = 50.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    TextButton(onClick = {
                        personalOverTimeViewModel.addExtraOverTime(hoursOverTimeInput.toInt())
                        onclickDialog()}
                    ) {
                        Text(text = " ثبت",
                            fontSize = 25.sp
                        )

                    }

                }
            }
        }
    }
}
@Composable
fun DialogGetOverTimeHours(
    onclickDialog : () ->  Unit,
    personalOverTimeViewModel: PersonalOverTimeViewModel,
    year: Int, month: Int, day: Int
    ){
    var hoursOverTimeInput by remember { mutableStateOf(0) }
    var showPick by remember { mutableStateOf(false) }


    Dialog(onDismissRequest = {onclickDialog()}) {

        Card (){
            Column(modifier = Modifier.padding(50.dp)) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Text(text = "${day.toPersianNumber()}/${month.toPersianMonth(LocalContext.current)}/${year.toPersianNumber()}")
                }
                Spacer(modifier = Modifier.padding(horizontal = 15.dp))

            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
                ){
//               extFieldNumber(overTimeInput = overTimeInput, label = R.string.InterHour , onValueChange = {hoursOverTimeInput != hoursOverTimeInput} , )
              Text(text = stringResource(id = R.string.InterHour),
                  modifier = Modifier.padding(start = 5.dp)
              )
                Spacer(modifier = Modifier.padding(horizontal = 15.dp))
                Text(text = stringResource(id =R.string.horus_tip,hoursOverTimeInput.toPersianNumber()) )

                Spacer(modifier = Modifier.padding(horizontal = 15.dp))
                Button(onClick = { showPick =  true }) {
                    Text(" آنالوگ")
                }
             }
                Spacer(modifier = Modifier.padding(vertical = 20.dp))
                Row {
                    TextButton(onClick = {
                        personalOverTimeViewModel.updateOverTime(year - 1400,month,day,hoursOverTimeInput)
                        onclickDialog()
                    }) {
                       Text(text ="ثبت" )
                    }
                    TextButton(onClick = {
                        personalOverTimeViewModel.updateOverTime(year - 1400,month,day, REQUEST_REJECTION)
                        onclickDialog()
                    }) {
                        Text(text ="پرسنل رد کرد")
                    }
                    TextButton(onClick = { }) {
                        Text(text ="ارسال پیامک" )
                    }
                }
            }
        }


        if (showPick) {
            val hour = 0
            val minute = 0
            val timePickerDetails = TimePickerDialog(
                LocalContext.current,
                { _, hour: Int, _ ->
                      hoursOverTimeInput = hour

                }, hour, minute, false
            )


            timePickerDetails.show()

            showPick = false


        }

    }
}




@Composable
fun PersonalDetailsScreen(
     personalDetails: PersonalDetails,
    modifier: Modifier =Modifier){

    Spacer(modifier = Modifier.padding(vertical = 10.dp))
Card (modifier = Modifier
    .fillMaxWidth()
    .padding(6.dp)){


    Column(modifier = modifier.padding(5.dp)) {
    Row (
        verticalAlignment = Alignment.CenterVertically){
        personalDetails.image?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

        }
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        Text(text = personalDetails.name)
        Spacer(modifier = Modifier.weight(1f))
        Card (shape = RoundedCornerShape(200.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)){
            Text(text = (personalDetails.currentMonthOvertime + personalDetails.overTimeAddCurrentMonth).toPersianNumber(),
                modifier =Modifier.padding(10 .dp))
        }
       }
        HorizontalDivider(modifier = Modifier.padding(5.dp), Dp(1f) , color = MaterialTheme.colorScheme.primary)
        Row {
            Text(text = stringResource(id = R.string.horus_month_current,personalDetails.currentMonthOvertime.toPersianNumber()))
        }
        HorizontalDivider(modifier = Modifier.padding(end = 150.dp, start = 5.dp, bottom = 5.dp), Dp(1f) , color = MaterialTheme.colorScheme.primary)
        Row {
            Text(text = stringResource(id = R.string.horus_add_month_current,personalDetails.overTimeAddCurrentMonth.toPersianNumber()))
        }
        HorizontalDivider(modifier = Modifier.padding(end = 150.dp, start = 5.dp, bottom = 5.dp), Dp(1f) , color = MaterialTheme.colorScheme.primary)
    }
}


}



@Composable
fun TitleCalendar(
    nextMonth: () -> Unit,
    previousMonth: () -> Unit,
    listMonth: List<CalendarModel>,
    modifier: Modifier = Modifier){
    val  context = LocalContext.current
Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceVariant),
) {
    IconButton(onClick = previousMonth) {
        Icon(
             imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_button)
        )
    }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)){
        Text(
            text = listMonth.last().iranianMonth.toPersianMonth(context),
            style = MaterialTheme.typography.titleLarge
        )
        Text(text = listMonth.last().iranianYear.toPersianNumber(),
            style = MaterialTheme.typography.titleMedium
        )
    }
    IconButton(onClick = nextMonth) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward ,
            contentDescription = stringResource(R.string.back_button)
        )
      }
   }
}




@Composable
fun CalendarItem(
    border:BorderStroke ,
    colorCardContainer:Color,
    hoursOverTime: Int,
    monthsDay: String,
    shift: String,
    modifier: Modifier = Modifier){
    var emptyDay by remember { mutableStateOf(true) }


    if (monthsDay == EMPTY_DATE.toPersianNumber()){
        emptyDay = false
    }
    val hoursOverTimeInput = when(hoursOverTime){
        0 -> ""
        EMPTY_OVERTIME -> "-"
        REQUEST_REJECTION -> "*"
        else -> stringResource(id = R.string.horus_tip,hoursOverTime.toPersianNumber())
    }

    Card (
        modifier = Modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = colorCardContainer),
        border = border,


    ){
        Column (modifier = modifier){
            Text(
                text = if(monthsDay == EMPTY_DATE.toPersianNumber()) "" else monthsDay,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.1.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,


            )

                Text(text = shift,
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.padding(2.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 8.sp
                )


            Text(
                text = if(emptyDay) hoursOverTimeInput else "" ,
                modifier = Modifier
                    .fillMaxWidth()
                    ,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,

            )
        }




    }

}




@Composable
fun CalendarScreenList(
    listMonth:List<CalendarModel>,
    onItemClick:  (year: Int, month: Int, day: Int) -> Unit
    ){
    Column {
        
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 8.dp)

         ) {

        items(listMonth) {
            CalendarItem(
                hoursOverTime = it.overTime,
                monthsDay = it.iranianDay.toPersianNumber(),
                colorCardContainer = if (it.isHolidayOccasion)
                MaterialTheme.colorScheme.error else if (it.isHolidayWeek)
                    MaterialTheme.colorScheme.errorContainer else
                    MaterialTheme.colorScheme.inversePrimary,
                shift = it.shift,
                border = if (it.today) BorderStroke(2.dp, color = Color.DarkGray)else
                BorderStroke(.5.dp , MaterialTheme.colorScheme.surfaceContainerLow),
                modifier = Modifier.clickable { onItemClick(it.iranianYear,it.iranianMonth,it.iranianDay) }

            )
        }
    }

    }
}





@Composable
fun CalendarHeaderWeek( modifier: Modifier =Modifier){
    val weekName = listOf(
        R.string.saturday,
        R.string.sunday,
        R.string.monday,
        R.string.tuesday,
        R.string.wednesdays,
        R.string.thursday,
        R.string.friday
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .padding(end = 14.dp, start = 14.dp, top = 8.dp)
            .fillMaxWidth()
    ){
        weekName.forEach{
        Card(
            modifier = Modifier
                .weight(1f)
            ,
            border = BorderStroke(1.dp, Color.Black)) {
            Text(
                text = stringResource(id = it),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
       }
    }
}






@Preview(showBackground = true)
@Composable
fun CalendarPreview(){
//DialogGetOverTimeHours({})
}







