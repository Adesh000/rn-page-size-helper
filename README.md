# @babacoder/rn-page-size-helper

A React Native module that detects Android 16KB Page Size compatibility by scanning your installed APK and checking each .so library for ELF alignment.

This helps you determine whether your app is compatible with devices using the new 16KB memory page size (Android 15+, some OEMs).

## Installation

```sh
npm install @babacoder/rn-page-size-helper
```

## Usage

```js
import { scan } from '@babacoder/rn-page-size-helper';

useEffect(() => {
  const runScan = async () => {
    try {
      const result = await scan();
      console.log('Scan result:', result);
    } catch (e) {
      console.error('Scan error:', e);
    }
  };

  runScan();
}, []);
```

## Contributing

- [Development workflow](CONTRIBUTING.md#development-workflow)
- [Sending a pull request](CONTRIBUTING.md#sending-a-pull-request)
- [Code of conduct](CODE_OF_CONDUCT.md)

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
