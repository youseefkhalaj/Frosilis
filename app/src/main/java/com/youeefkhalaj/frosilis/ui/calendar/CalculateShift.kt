package com.youeefkhalaj.frosilis.ui.calendar

import com.youeefkhalaj.frosilis.data.Shift

class CalculateShift(val shift: Shift) {


    fun calculateDaysYear(year: Int, month: Int, day: Int ): String {
        val y = year - 1401
        val y1 = if ( y >= 12){ (y * 365) + 3}
        else if (y >= 8 ){(y * 365) + 2}
        else if (y >= 3){(y * 365) + 1}
        else{y * 365}
        val m:Int
        val m1 = if ( month <= 6 ){month * 31}
        else{
            m = month - 6
            (m * 31) + 186
        }
        val sum = (y1 + m1 + day) % 8
        val returnShift:String
        if (shift == Shift.A){
            returnShift =  when (sum){
                0,1 -> "ص"
                2,3 -> "ع"
                4,5 -> "ش"
                else -> "ا"
            }
        }else if (shift == Shift.B){
            returnShift = when (sum){
                0,1 -> "ص"
                2,3 -> "ع"
                4,5 -> "ش"
                else -> "ا"
            }
        }else if (shift == Shift.C){
            returnShift =  when (sum){
                0,1 -> "ص"
                2,3 -> "ع"
                4,5 -> "ش"
                else -> "ا"
            }
        }else if (shift == Shift.D){
            returnShift = when (sum){
                0,1 -> "ص"
                2,3 -> "ع"
                4,5 -> "ش"
                else -> "ا"
            }
        }else{
            returnShift = when (sum){
                0,1 -> "ص"
                2,3 -> "ع"
                4,5 -> "ش"
                else -> "ا"
            }
        }
        return returnShift

    }
}