
package jetbrains.buildServer.cmakerunner.server;


/**
 * @author : Vladislav.Rassokhin
 */

import jetbrains.buildServer.cmakerunner.MakeRunnerConstants;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class MakeRunnerRunType extends RunType {

  private final PluginDescriptor myPluginDescriptor;

  public MakeRunnerRunType(@NotNull final RunTypeRegistry runTypeRegistry,
                           @NotNull final PluginDescriptor descriptor) {
    runTypeRegistry.registerRunType(this);
    myPluginDescriptor = descriptor;
  }

  @NotNull
  @Override
  public String getType() {
    return MakeRunnerConstants.TYPE;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return MakeRunnerConstants.DISPLAY_NAME;
  }

  @NotNull
  @Override
  public String getDescription() {
    return MakeRunnerConstants.DESCRIPTION;
  }

  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    // Do nothing
    return null;
  }

  @Override
  public String getEditRunnerParamsJspFilePath() {
    return myPluginDescriptor.getPluginResourcesPath("editMakeRunner.jsp");
  }

  @Override
  public String getViewRunnerParamsJspFilePath() {
    return myPluginDescriptor.getPluginResourcesPath("viewMakeRunner.jsp");
  }

  @Override
  public Map<String, String> getDefaultRunnerProperties() {
    return null;
  }
}