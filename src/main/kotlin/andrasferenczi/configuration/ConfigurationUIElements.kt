package andrasferenczi.configuration

import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JTextField

// Output
class ConfigurationUIElements(
    val jComponent: JComponent,

    val copyWithNameTextField: JTextField,
    val toMapMethodNameTextField: JTextField,
    val fromMapMethodNameTextField: JTextField,
    val useRequiredAnnotationCheckBox: JCheckBox,
    val useNewKeywordCheckbox: JCheckBox,
    val useConstKeywordForConstructorCheckbox: JCheckBox,
    val optimizeConstCopyCheckbox: JCheckBox,
    val addKeyMapperForMapCheckbox: JCheckBox,
    val noImplicitCastsCheckbox: JCheckBox
) {


    fun extractCurrentConfigurationData() : ConfigurationData {
        return ConfigurationData(
            copyWithMethodName = copyWithNameTextField.text,
            toMapMethodName = toMapMethodNameTextField.text,
            fromMapMethodName = fromMapMethodNameTextField.text,
            useRequiredAnnotation = useRequiredAnnotationCheckBox.isSelected,
            useNewKeyword = useNewKeywordCheckbox.isSelected,
            useConstForConstructor = useConstKeywordForConstructorCheckbox.isSelected,
            optimizeConstCopy = optimizeConstCopyCheckbox.isSelected,
            addKeyMapperForMap = addKeyMapperForMapCheckbox.isSelected,
            noImplicitCasts = noImplicitCastsCheckbox.isSelected
        )
    }

    fun setFields(configurationData: ConfigurationData) {
        copyWithNameTextField.text = configurationData.copyWithMethodName
        toMapMethodNameTextField.text = configurationData.toMapMethodName
        fromMapMethodNameTextField.text = configurationData.fromMapMethodName
        useRequiredAnnotationCheckBox.isSelected = configurationData.useRequiredAnnotation
        useNewKeywordCheckbox.isSelected = configurationData.useNewKeyword
        useConstKeywordForConstructorCheckbox.isSelected = configurationData.useConstForConstructor
        optimizeConstCopyCheckbox.isSelected = configurationData.optimizeConstCopy
        addKeyMapperForMapCheckbox.isSelected = configurationData.addKeyMapperForMap
        noImplicitCastsCheckbox.isSelected = configurationData.noImplicitCasts
    }

}