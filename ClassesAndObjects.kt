import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class SmartDevice protected constructor(val name:String, val category:String){
    var deviceStatus = "online"
    protected  set
    open val deviceType = "unknown"


    open fun turnOn(){
        deviceStatus = "on"
    }
    open fun turnOff(){
        deviceStatus = "off"
    }
    open fun printDeviceInfo(){
        println("Device name: $name, category : $category,type : $deviceType")
    }
}

class SmartTvDevice (deviceName: String,deviceCategory: String)
    :SmartDevice(name = deviceName, category=deviceCategory){
        override val deviceType = "Smart Tv"
       private var speakerVolume by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)

       private var channelNumber by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)

    fun increaseSpeakerVolume(){
        speakerVolume++
        println("Speaker volume increased to $speakerVolume")
    }

    fun decreaseVolume(){
        speakerVolume--
        println("Speaker volume decreased to $speakerVolume")
    }

     fun nextChannel(){
        channelNumber++
        println("Channel number increased to $channelNumber")
    }
    fun previousChannel(){
        channelNumber--
        println("Channel number decreased to $channelNumber")
    }

    override fun turnOn(){
        super.turnOn()
        println(
            "$name is turned on. Speaker volume is set to $speakerVolume and channel number is " +
                    "set to $channelNumber."
        )
    }
    override  fun turnOff(){
        super.turnOff()
        println("$name turned off")
    }

    override fun printDeviceInfo(){
        super.printDeviceInfo()
    }


}

class SmartLightDevice(deviceName: String,deviceCategory: String)
    :SmartDevice(name =deviceName,category = deviceCategory){
         override val deviceType = "Smart Light"

    private var  brightnessLevel by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)


    fun increaseBrightness(){
        brightnessLevel++
        println("brightness increased to $brightnessLevel")
    }

    fun decreaseBrightness(){
        brightnessLevel--
        println("brightness decreased to $brightnessLevel")
    }

    override fun turnOn() {
        super.turnOn()
        brightnessLevel = 2
        println("$name turned on. The brightness level is $brightnessLevel.")
    }

    override fun turnOff() {
        super.turnOff()
        brightnessLevel = 0
        println("Smart Light turned off")
    }

    override fun printDeviceInfo(){
        super.printDeviceInfo()

    }
    }

// The SmartHome class HAS-A smart TV device and smart light.
class SmartHome(
    val smartTvDevice: SmartTvDevice,
    val smartLightDevice: SmartLightDevice

){
    var deviceTurnOnCount by  RangeRegulator(initialValue = 0, minValue = 0, maxValue = Int.MAX_VALUE)
    private set

    fun turnOnTv(){
        deviceTurnOnCount++
        smartTvDevice.turnOn()
    }
  fun turnOffTv(){
      deviceTurnOnCount--
      smartTvDevice.turnOff()
  }
    fun turnOnLight(){
        smartLightDevice.turnOn()
        deviceTurnOnCount++
    }
    fun turnOffLight(){
        smartLightDevice.turnOff()
        deviceTurnOnCount--
    }

    fun changeTvChannelToNext(){
        if(smartTvDevice.deviceStatus =="on")
            smartTvDevice.nextChannel()
    }
    fun changeTvChannelToPrevious(){
        if(smartTvDevice.deviceStatus =="on")
            smartTvDevice.previousChannel()
    }
    fun increaseSpeakerVolume(){
        if(smartTvDevice.deviceStatus =="on")
            smartTvDevice.increaseSpeakerVolume()
    }
    fun decreaseTvVolume(){
        if(smartTvDevice.deviceStatus =="on")
            smartTvDevice.decreaseVolume()
    }

    fun decreaseLightBrightness(){
        if(smartLightDevice.deviceStatus =="on")
            smartLightDevice.decreaseBrightness()
    }
    fun increaseLightBrightness() {
        if(smartLightDevice.deviceStatus =="on")
            smartLightDevice.increaseBrightness()
    }

    fun turnOffAllDevices(){
        turnOffTv()
        turnOffLight()
    }

    fun printSmartTvInfo(){
        smartTvDevice.printDeviceInfo()
    }
    fun printSmartLightInfo(){
        smartLightDevice.printDeviceInfo()
    }
}
class RangeRegulator(
    initialValue:Int,
    private val minValue:Int,
    private val maxValue:Int
):ReadWriteProperty<Any?,Int>{
    private var fieldData = initialValue
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return fieldData
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}

fun  main() {
    val smartDevice:SmartTvDevice = SmartTvDevice(deviceName = "Android Tv", deviceCategory = "Entertainment")
    val smartLightDevice:SmartLightDevice = SmartLightDevice("Google light","Utility")

    val smartHome:SmartHome = SmartHome(smartDevice,smartLightDevice)

    smartHome.turnOnTv()
    smartHome.turnOnLight()
    println("Number of smart home active devices ${smartHome.deviceTurnOnCount}")
    println()

    smartHome.decreaseTvVolume()
    smartHome.changeTvChannelToPrevious()
    smartHome.decreaseLightBrightness()

    println()

    smartHome.increaseSpeakerVolume()
    smartHome.changeTvChannelToNext()
    smartHome.increaseLightBrightness()
    println()

    smartHome.turnOffTv()
    println("Number of smart home active devices ${smartHome.deviceTurnOnCount}")
    println()

    smartHome.turnOffAllDevices()
    println("Number of smart home active devices ${smartHome.deviceTurnOnCount}")
    println()

    smartHome.printSmartTvInfo()
    smartHome.printSmartLightInfo()


}
