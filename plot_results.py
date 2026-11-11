import os
import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

# Установка стиля графиков
sns.set_theme(style="whitegrid", palette="muted")
plt.rcParams.update({"font.size": 11, "figure.autolayout": True})


def generate_sample_data_if_missing(file_path):
    """Создаёт тестовый CSV, если ExperimentRunner ещё не запускался."""
    if os.path.exists(file_path):
        return

    os.makedirs(os.path.dirname(file_path), exist_ok=True)
    rows = ["algorithm,input,n,pattern_length,time_ms,counter,answer"]

    for n in [4, 6, 8, 10]:
        ops = int(3**n)
        rows.append(f"recursive,random_pair,{n},0,{ops * 0.00005:.6f},{ops},2")

    for n in [100, 200, 400, 800]:
        ops = n * n
        rows.append(f"table,random_pair,{n},0,{ops * 0.000015:.6f},{ops},35")
        rows.append(f"two_rows,random_pair,{n},0,{ops * 0.000008:.6f},{ops},35")

    for n in [10000, 20000, 40000, 80000]:
        m = 64
        ops_naive = (n - m + 1) * m
        rows.append(
            f"naive,repetitive_worst,{n},{m},{ops_naive * 0.0000002:.6f},{ops_naive},0"
        )
        rows.append(
            f"kmp,repetitive_worst,{n},{m},{n * 0.00000005:.6f},{n},0"
        )

    with open(file_path, "w", encoding="utf-8") as f:
        f.write("\n".join(rows))


def plot_experiments(csv_path="results/results.csv"):
    generate_sample_data_if_missing(csv_path)
    df = pd.read_csv(csv_path)

    os.makedirs("results/plots", exist_ok=True)

    # 1. Edit Distance - Dynamic Programming (Table vs Two Rows)
    df_edit_dp = df[df["algorithm"].isin(["table", "two_rows"])]
    if not df_edit_dp.empty:
        fig, axes = plt.subplots(1, 2, figsize=(14, 5))

        sns.lineplot(
            data=df_edit_dp,
            x="n",
            y="time_ms",
            hue="algorithm",
            marker="o",
            linewidth=2.5,
            ax=axes[0],
        )
        axes[0].set_title("Edit Distance (DP): Time vs String Length (n)")
        axes[0].set_xlabel("String Length (n)")
        axes[0].set_ylabel("Execution Time (ms)")

        sns.lineplot(
            data=df_edit_dp,
            x="n",
            y="counter",
            hue="algorithm",
            style="algorithm",
            markers=True,
            ax=axes[1],
        )
        axes[1].set_title("Edit Distance (DP): Operations Count O(n^2)")
        axes[1].set_xlabel("String Length (n)")
        axes[1].set_ylabel("Operation Counter")

        plt.suptitle(
            "Edit Distance: DP Table vs Two Rows",
            fontsize=14,
            fontweight="bold",
        )
        plt.tight_layout()
        plt.savefig("results/plots/edit_distance_dp.png", dpi=300)
        plt.show()

    # 2. Edit Distance - Recursive
    df_rec = df[df["algorithm"] == "recursive"]
    if not df_rec.empty:
        fig, axes = plt.subplots(1, 2, figsize=(14, 5))

        sns.lineplot(
            data=df_rec,
            x="n",
            y="time_ms",
            color="crimson",
            marker="s",
            linewidth=2.5,
            ax=axes[0],
        )
        axes[0].set_title("Recursive Edit Distance: Execution Time")
        axes[0].set_xlabel("String Length (n)")
        axes[0].set_ylabel("Execution Time (ms)")

        sns.lineplot(
            data=df_rec,
            x="n",
            y="counter",
            color="darkred",
            marker="s",
            linewidth=2.5,
            ax=axes[1],
        )
        axes[1].set_title("Recursive Edit Distance: Function Calls")
        axes[1].set_xlabel("String Length (n)")
        axes[1].set_ylabel("Call Counter")

        plt.suptitle(
            "Edit Distance: Naive Recursive Performance",
            fontsize=14,
            fontweight="bold",
        )
        plt.tight_layout()
        plt.savefig("results/plots/edit_distance_recursive.png", dpi=300)
        plt.show()

    # 3. String Search: Naive vs KMP
    df_search = df[df["algorithm"].isin(["naive", "kmp"])]
    if not df_search.empty:
        fig, axes = plt.subplots(1, 2, figsize=(14, 5))

        sns.lineplot(
            data=df_search,
            x="n",
            y="time_ms",
            hue="algorithm",
            style="input",
            markers=True,
            linewidth=2,
            ax=axes[0],
        )
        axes[0].set_title("String Search: Time vs Text Length (n)")
        axes[0].set_xlabel("Text Length (n)")
        axes[0].set_ylabel("Execution Time (ms)")

        sns.lineplot(
            data=df_search,
            x="n",
            y="counter",
            hue="algorithm",
            style="input",
            markers=True,
            linewidth=2,
            ax=axes[1],
        )
        axes[1].set_title("String Search: Character Comparisons")
        axes[1].set_xlabel("Text Length (n)")
        axes[1].set_ylabel("Comparisons Count")

        plt.suptitle(
            "Pattern Matching: Naive vs KMP",
            fontsize=14,
            fontweight="bold",
        )
        plt.tight_layout()
        plt.savefig("results/plots/string_search_kmp_vs_naive.png", dpi=300)
        plt.show()


if __name__ == "__main__":
    plot_experiments()