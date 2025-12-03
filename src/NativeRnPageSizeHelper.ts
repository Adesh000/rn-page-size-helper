import { TurboModuleRegistry, type TurboModule } from 'react-native';

export interface Spec extends TurboModule {
  scan(): Promise<{
    devicePageSize: number;
    isDevice16KB: boolean;
    abiFolders: string[];
    scannedLibraries: {
      name: string;
      path: string;
      isCompatible: boolean;
      pageSizeInElf: number;
    }[];
    hasIncompatibleLibs: boolean;
  }>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RnPageSizeHelper');
