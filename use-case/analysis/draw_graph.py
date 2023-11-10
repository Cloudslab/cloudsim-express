import seaborn as sns
import matplotlib.pyplot as plt
import pandas as pd

from matplotlib.pyplot import figure

figure(figsize=(8, 3), dpi=80)

df = pd.read_csv('human-interpret-time.csv')

colors = ['green', 'blue', 'purple']
plt.bar(df['Approach'], df['Time(ms)'], color=colors, width=0.5, edgecolor="black", linewidth=1.5)
# plt.title('Country Vs GDP Per Capita', fontsize=14)
# plt.xlabel('', fontsize=14)
plt.xticks(fontsize=14.4)
plt.yticks(fontsize=14.4)
plt.ylabel('Time (ms)', fontsize=20)
plt.grid(False)
plt.legend()

# Displaying the plot
plt.savefig("interpret-times.png")
plt.savefig("interpret-times.svg")
