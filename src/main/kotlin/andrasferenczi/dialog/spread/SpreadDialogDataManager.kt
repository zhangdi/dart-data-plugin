package andrasferenczi.dialog.spread

interface SpreadDialogDataManager {

    // Getter and setter for always getting fresh data
    var data: SpreadDialogUIData


    fun update(updater: SpreadDialogUIData.() -> SpreadDialogUIData) {
        this.data = this.data.updater()
    }
}
