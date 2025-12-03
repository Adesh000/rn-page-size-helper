import { Text, View, StyleSheet, ScrollView } from 'react-native';
import { scan } from '@babacoder/rn-page-size-helper';
import { useEffect, useState } from 'react';

export default function App() {
  const [result, setResult] = useState<any>(null);

  useEffect(() => {
    const getScanned = async () => {
      try {
        const scannedResult = await scan();
        console.log('🚀 ~ App ~ result:', scannedResult);
        setResult(scannedResult);
      } catch (error) {
        console.error('🚀 ~ App ~ error:', error);
      }
    };

    getScanned();
  }, []);

  return (
    <View style={styles.container}>
      <ScrollView contentContainerStyle={{ padding: 20 }}>
        <Text style={{ fontSize: 22, fontWeight: 'bold' }}>
          16KB Page Size Compatibility Test
        </Text>

        {result ? (
          <Text style={{ marginTop: 20 }}>
            {JSON.stringify(result, null, 2)}
          </Text>
        ) : (
          <Text style={{ marginTop: 20 }}>Scanning...</Text>
        )}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
