@file:Suppress("DEPRECATION")

package com.youeefkhalaj.frosilis.ui.item

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.youeefkhalaj.frosilis.FrosilisTopAppBar
import com.youeefkhalaj.frosilis.R
import com.youeefkhalaj.frosilis.data.Shift
import com.youeefkhalaj.frosilis.ui.AppViewModelProvider
import com.youeefkhalaj.frosilis.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object PersonalItemEntryDestination : NavigationDestination {
    override val route = "item_entry"
    override val titleRes = R.string.item_entry_title
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalItemEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: PersonalEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            FrosilisTopAppBar(
                title = stringResource(PersonalItemEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        ItemEntryBody(
            personalUiState = viewModel.personalUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.savePersonal()
                    navigateBack()
                }
            }
            ,
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }


}




@Composable
fun ItemEntryBody(
    personalUiState: PersonalUiState,
    onItemValueChange: (PersonalDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ItemInputForm(
            personaDetails = personalUiState.personalDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = personalUiState.isEntryValid
        )
        Button(
            onClick = onSaveClick,
            enabled = personalUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}


@Composable
fun ItemInputForm(
    personaDetails: PersonalDetails,
    modifier: Modifier = Modifier,
    onValueChange: (PersonalDetails) -> Unit = {},
    enabled: Boolean
) {


    var imageUri by remember{ mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

        val bitmap = remember {
            mutableStateOf<Bitmap?>(null)
        }




        imageUri?.let {
            if(Build.VERSION.SDK_INT < 28){
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver,it)
            }else{
                val source = ImageDecoder.createSource(context.contentResolver,it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
            val bitmap1 = makeSmall(bitmap.value,400)

            onValueChange(
                personaDetails.copy(image = bitmap1)
            )

    }

        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }






    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (personaDetails.image == null){

            Image(
                painter = painterResource(id = R.drawable.blank_profile_picture_973460_1920),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop)

        }else {


                Image(
                    bitmap = personaDetails.image.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable { launcher.launch("image/*") },
                    contentScale = ContentScale.Crop
                )
            }
        }

     
        OutlinedTextField(
            value = personaDetails.name,
            onValueChange = { onValueChange(personaDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.item_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
           // enabled = enabled,
            singleLine = true
        )


        }


            val shiftList = listOf("A","B","C","D","F")

        Card {
            Text(
                text = stringResource(R.string.shift),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
            Row (horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                shiftList.forEach{ shift ->
                    Text(text = if (shift == "F") "ثابت" else shift)
                    RadioButton(
                        selected = shift == personaDetails.shift.toString(),
                        onClick = {
                            onValueChange(personaDetails.copy(shift = when (shift){
                                "A" -> Shift.A
                                "B" -> Shift.B
                                "C" -> Shift.C
                                "D" -> Shift.D
                                else -> Shift.F
                            }
                        )) },
                        )


                }
              }

          }
    if (!enabled) {
        Text(
            text = stringResource(R.string.required_fields),
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
        )
    }
    }




fun makeSmall(image: Bitmap?, maxSize: Int): Bitmap {
    var width = image!!.width
    var height = image.height
    val ratio = width.toFloat() / height.toFloat()
    if (ratio > 1) {
        width = maxSize
        height = (width / ratio).toInt()
    } else {
        height = maxSize
        width = (height * ratio).toInt()
    }
    return Bitmap.createScaledBitmap(image, width, height, true)

}


