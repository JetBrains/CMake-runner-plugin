
package jetbrains.buildServer.cmakerunner.server;

/**
 * @author : Vladislav.Rassokhin
 */

import jetbrains.buildServer.cmakerunner.CMakeConfigureConstants;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class CMakeConfigureRunType extends RunType {

  private final PluginDescriptor myPluginDescriptor;

  public CMakeConfigureRunType(@NotNull final RunTypeRegistry runTypeRegistry,
                               @NotNull final PluginDescriptor descriptor) {
    runTypeRegistry.registerRunType(this);
    myPluginDescriptor = descriptor;
  }

  @NotNull
  @Override
  public String getType() {
    return CMakeConfigureConstants.TYPE;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return CMakeConfigureConstants.DISPLAY_NAME;
  }

  @NotNull
  @Override
  public String getDescription() {
    return CMakeConfigureConstants.DESCRIPTION;
  }

  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    // Do nothing
    return null;
  }

  @Override
  public String getEditRunnerParamsJspFilePath() {
    return myPluginDescriptor.getPluginResourcesPath("editCMakeConfiguratorRunner.jsp");
  }

  @Override
  public String getViewRunnerParamsJspFilePath() {
    return myPluginDescriptor.getPluginResourcesPath("viewCMakeConfiguratorRunner.jsp");
  }

  @Override
  public Map<String, String> getDefaultRunnerProperties() {
    final String trueStr = Boolean.toString(true);
//    final String falseStr = Boolean.toString(false);

    final Map<String, String> ret = new HashMap<String, String>();

    ret.put(CMakeConfigureConstants.UI_REDIRECT_STDERR, trueStr);

    ret.put(CMakeConfigureConstants.UI_DEVELOPER_WARNINGS, trueStr);
    ret.put(CMakeConfigureConstants.UI_WARN_UNINITIALIZED, trueStr);
    ret.put(CMakeConfigureConstants.UI_WARN_UNUSED_VARS, trueStr);
//    ret.put(CMakeRunnerConstants.UI_PRINT_TRACE, falseStr);
//    ret.put(CMakeRunnerConstants.UI_DEBUG_MODE, falseStr);
//    ret.put(CMakeRunnerConstants.UI_NO_WARN_UNUSED_CLI, falseStr);

    ret.put(CMakeConfigureConstants.UI_MAKEFILE_GENERATOR, "Default");

    return ret;
  }
}